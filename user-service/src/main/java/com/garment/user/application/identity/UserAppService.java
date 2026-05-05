package com.garment.user.application.identity;

import com.garment.common.domain.BizException;
import com.garment.common.domain.DomainEvent;
import com.garment.common.domain.JwtUtils;
import com.garment.user.application.identity.dto.LoginCommand;
import com.garment.user.application.identity.dto.RegisterCommand;
import com.garment.user.domain.identity.entity.User;
import com.garment.user.domain.identity.repository.UserRepository;
import com.garment.user.domain.identity.vo.Email;
import com.garment.user.domain.identity.vo.Phone;
import com.garment.user.infrastructure.event.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户身份应用服务
 * <p>
 * 编排用户注册、登录等用例，协调领域对象和基础设施完成业务流程。
 * 应用层不包含业务规则，仅负责流程编排和事务管理。
 * </p>
 */
@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final DomainEventPublisher eventPublisher;

    /**
     * 用户注册
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterCommand cmd) {
        // 1. 校验用户名唯一性
        if (userRepository.existsByUsername(cmd.getUsername())) {
            throw new BizException("用户名已存在");
        }

        // 2. 构建值对象
        Phone phone = cmd.getPhone() != null && !cmd.getPhone().isEmpty()
                ? new Phone(cmd.getPhone()) : null;
        Email email = cmd.getEmail() != null && !cmd.getEmail().isEmpty()
                ? new Email(cmd.getEmail()) : null;

        // 3. 校验手机号/邮箱唯一性
        if (phone != null && userRepository.existsByPhone(phone.getValue())) {
            throw new BizException("手机号已被注册");
        }
        if (email != null && userRepository.existsByEmail(email.getValue())) {
            throw new BizException("邮箱已被注册");
        }

        // 4. 加密密码
        String encodedPassword = passwordEncoder.encode(cmd.getPassword());

        // 5. 调用领域方法创建用户
        User user = User.create(cmd.getUsername(), encodedPassword, cmd.getNickname(), phone, email);

        // 6. 持久化
        User savedUser = userRepository.save(user);

        // 7. 发布领域事件
        String registerType = phone != null ? "PHONE" : "EMAIL";
        DomainEvent event = savedUser.registeredEvent(registerType);
        eventPublisher.publish(event);

        return savedUser;
    }

    /**
     * 用户登录
     */
    @Transactional(readOnly = true)
    public Map<String, Object> login(LoginCommand cmd, String loginIp) {
        // 1. 根据用户名查找用户
        User user = userRepository.findByUsername(cmd.getUsername())
                .orElseThrow(() -> new BizException("用户名或密码错误"));

        // 2. 校验密码
        if (!user.checkPassword(cmd.getPassword(), passwordEncoder)) {
            throw new BizException("用户名或密码错误");
        }

        // 3. 校验账号状态
        if (!user.canLogin()) {
            throw new BizException("账号已被冻结或注销，无法登录");
        }

        // 4. 生成 JWT Token
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("username", user.getUsername());
        claims.put("tenantId", user.getTenantId());
        String token = jwtUtils.generateToken(user.getUsername(), claims);

        // 5. 记录登录信息（调用领域方法）
        user.recordLogin(loginIp);
        userRepository.save(user);

        // 6. 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("username", user.getUsername());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        return result;
    }

    /**
     * 根据 ID 获取用户信息
     */
    @Transactional(readOnly = true)
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BizException("用户不存在"));
    }

    /**
     * 修改密码
     */
    @Transactional(rollbackFor = Exception.class)
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BizException("用户不存在"));

        if (!user.checkPassword(oldPassword, passwordEncoder)) {
            throw new BizException("原密码错误");
        }

        String newEncodedPassword = passwordEncoder.encode(newPassword);
        user.changePassword(newEncodedPassword);
        userRepository.save(user);
    }

    /**
     * 更新用户资料
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(Long userId, String nickname, String avatar) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BizException("用户不存在"));
        user.updateProfile(nickname, avatar);
        userRepository.save(user);
    }

    /**
     * 冻结用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void freezeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BizException("用户不存在"));
        user.freeze();
        userRepository.save(user);
    }

    /**
     * 解冻用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void unfreezeUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BizException("用户不存在"));
        user.unfreeze();
        userRepository.save(user);
    }

    /**
     * 注销用户
     */
    @Transactional(rollbackFor = Exception.class)
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BizException("用户不存在"));
        user.deactivate();
        userRepository.save(user);
    }
}
