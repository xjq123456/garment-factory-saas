package com.garment.user.domain.identity.entity;

import com.garment.common.domain.AggregateRoot;
import com.garment.common.infrastructure.BaseEntity;
import com.garment.common.domain.BizException;
import com.garment.user.domain.identity.event.UserRegisteredEvent;
import com.garment.user.domain.identity.vo.Email;
import com.garment.user.domain.identity.vo.Phone;
import com.garment.user.domain.identity.vo.UserStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户聚合根
 * <p>
 * 用户是身份域的核心聚合根，承载用户账号的全部业务逻辑。
 * 通过继承 {@link BaseEntity} 获得持久化能力（雪花主键、多租户、审计字段等），
 * 通过组合 {@link AggregateRoot} 获得领域事件管理能力。
 * </p>
 * <p>
 * 核心职责：
 * <ul>
 *   <li>用户注册（创建账号、发布注册事件）</li>
 *   <li>密码管理（加密存储、密码校验）</li>
 *   <li>账号状态管理（冻结/解冻/注销）</li>
 *   <li>登录信息更新（最后登录时间、登录IP）</li>
 * </ul>
 * </p>
 *
 * @author garment-factory-saas
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    /** 聚合根事件管理（通过组合方式） */
    private final transient AggregateRoot aggregateRoot = new AggregateRoot() {};

    // ==================== 基本信息 ====================

    /** 用户名（全局唯一） */
    private String username;

    /** 密码（BCrypt 加密存储） */
    private String password;

    /** 昵称 */
    private String nickname;

    /** 头像 URL */
    private String avatar;

    // ==================== 联系方式 ====================

    /** 手机号（可选） */
    private Phone phone;

    /** 邮箱（可选） */
    private Email email;

    // ==================== 状态信息 ====================

    /** 用户状态 */
    private UserStatus status;

    /** 最后登录时间 */
    private java.time.LocalDateTime lastLoginAt;

    /** 最后登录 IP */
    private String lastLoginIp;

    // ==================== 领域行为 ====================

    /**
     * 创建新用户（静态工厂方法）
     * <p>
     * 执行用户注册的业务规则校验：
     * 1. 用户名和密码不能为空
     * 2. 用户名长度 4-32 位，仅允许字母、数字、下划线
     * 3. 手机号和邮箱至少提供一种
     * 4. 设置初始状态为 ACTIVE
     * 5. 发布 UserRegisteredEvent 领域事件
     * </p>
     *
     * @param username 用户名
     * @param password 加密后的密码
     * @param nickname 昵称
     * @param phone    手机号（可选）
     * @param email    邮箱（可选）
     * @return 新创建的用户实例
     * @throws BizException 当业务规则校验失败时抛出
     */
    public static User create(String username, String password, String nickname,
                              Phone phone, Email email) {
        // 参数校验
        if (username == null || username.trim().isEmpty()) {
            throw new BizException("用户名不能为空");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new BizException("密码不能为空");
        }
        if (username.length() < 4 || username.length() > 32) {
            throw new BizException("用户名长度必须在 4-32 位之间");
        }
        if (!username.matches("^[a-zA-Z0-9_]+$")) {
            throw new BizException("用户名仅允许字母、数字和下划线");
        }
        if (phone == null && email == null) {
            throw new BizException("手机号和邮箱至少提供一种");
        }

        // 构建用户实例
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setNickname(nickname != null ? nickname : username);
        user.setPhone(phone);
        user.setEmail(email);
        user.setStatus(UserStatus.ACTIVE);

        // 发布注册事件
        String registerType = phone != null ? "PHONE" : "EMAIL";
        user.aggregateRoot.registerEvent(
                new UserRegisteredEvent(user.getId(), username, user.getTenantId(), registerType)
        );

        return user;
    }

    /**
     * 校验密码是否匹配
     * <p>
     * 由应用层调用，传入明文密码和 BCrypt 校验器。
     * </p>
     *
     * @param rawPassword    明文密码
     * @param passwordEncoder BCrypt 密码编码器
     * @return true 表示密码匹配
     */
    public boolean checkPassword(String rawPassword, org.springframework.security.crypto.password.PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(rawPassword, this.password);
    }

    /**
     * 冻结用户账号
     * <p>
     * 仅 ACTIVE 状态的用户可以被冻结。
     * </p>
     *
     * @throws BizException 当用户状态不允许冻结时抛出
     */
    public void freeze() {
        if (this.status != UserStatus.ACTIVE) {
            throw new BizException("仅正常状态的用户可以被冻结");
        }
        this.status = UserStatus.FROZEN;
    }

    /**
     * 解冻用户账号
     * <p>
     * 仅 FROZEN 状态的用户可以被解冻。
     * </p>
     *
     * @throws BizException 当用户状态不允许解冻时抛出
     */
    public void unfreeze() {
        if (this.status != UserStatus.FROZEN) {
            throw new BizException("仅冻结状态的用户可以被解冻");
        }
        this.status = UserStatus.ACTIVE;
    }

    /**
     * 注销用户（逻辑删除）
     * <p>
     * 标记用户为已删除状态。ACTIVE 和 FROZEN 状态均可执行注销。
     * </p>
     *
     * @throws BizException 当用户已经是删除状态时抛出
     */
    public void deactivate() {
        if (this.status == UserStatus.DELETED) {
            throw new BizException("用户已经是注销状态");
        }
        this.status = UserStatus.DELETED;
    }

    /**
     * 更新最后登录信息
     * <p>
     * 在用户成功登录后调用，记录登录时间和 IP 地址。
     * </p>
     *
     * @param loginIp 登录 IP 地址
     */
    public void recordLogin(String loginIp) {
        this.lastLoginAt = java.time.LocalDateTime.now();
        this.lastLoginIp = loginIp;
    }

    /**
     * 修改密码
     * <p>
     * 设置新的加密密码。由应用层负责 BCrypt 加密。
     * </p>
     *
     * @param newEncodedPassword 新的加密密码
     * @throws BizException 当新密码为空时抛出
     */
    public void changePassword(String newEncodedPassword) {
        if (newEncodedPassword == null || newEncodedPassword.trim().isEmpty()) {
            throw new BizException("新密码不能为空");
        }
        this.password = newEncodedPassword;
    }

    /**
     * 更新用户资料
     *
     * @param nickname 新昵称（可选，为 null 则不更新）
     * @param avatar   新头像 URL（可选，为 null 则不更新）
     */
    public void updateProfile(String nickname, String avatar) {
        if (nickname != null && !nickname.trim().isEmpty()) {
            this.nickname = nickname;
        }
        if (avatar != null && !avatar.trim().isEmpty()) {
            this.avatar = avatar;
        }
    }

    /**
     * 判断当前用户是否可以登录
     *
     * @return true 表示可以登录
     */
    public boolean canLogin() {
        return this.status != null && this.status.canLogin();
    }

    /**
     * 拉取并清空领域事件
     *
     * @return 领域事件列表
     */
    public java.util.List<com.garment.common.domain.DomainEvent> pullEvents() {
        return aggregateRoot.pullEvents();
    }
}