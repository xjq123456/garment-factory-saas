package com.garment.user.domain.identity.repository;

import com.garment.user.domain.identity.entity.User;

import java.util.Optional;

/**
 * 用户仓储接口（领域层）
 * <p>
 * 定义用户聚合根的持久化契约。领域层仅声明接口，
 * 具体的数据库实现由 infrastructure 层提供。
 * 这样保证领域层不依赖任何基础设施细节。
 * </p>
 *
 * @author garment-factory-saas
 */
public interface UserRepository {

    /**
     * 保存用户（新增或更新）
     *
     * @param user 用户聚合根
     * @return 保存后的用户（包含生成的ID等）
     */
    User save(User user);

    /**
     * 根据用户ID查找用户
     *
     * @param id 用户ID
     * @return 用户（可能为空）
     */
    Optional<User> findById(Long id);

    /**
     * 根据用户名查找用户
     *
     * @param username 用户名
     * @return 用户（可能为空）
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据手机号查找用户
     *
     * @param phone 手机号
     * @return 用户（可能为空）
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据邮箱查找用户
     *
     * @param email 邮箱地址
     * @return 用户（可能为空）
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否已存在
     *
     * @param username 用户名
     * @return true 表示已存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查手机号是否已存在
     *
     * @param phone 手机号
     * @return true 表示已存在
     */
    boolean existsByPhone(String phone);

    /**
     * 检查邮箱是否已存在
     *
     * @param email 邮箱地址
     * @return true 表示已存在
     */
    boolean existsByEmail(String email);
}