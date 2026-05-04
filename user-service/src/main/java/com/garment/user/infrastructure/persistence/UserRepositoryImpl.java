package com.garment.user.infrastructure.persistence;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.garment.user.domain.identity.entity.User;
import com.garment.user.domain.identity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 用户仓储实现（基础设施层）
 * <p>
 * 负责领域实体 User 与数据对象 UserDO 之间的转换，
 * 以及调用 MyBatis Plus Mapper 完成数据库操作。
 * </p>
 */
@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserMapper userMapper;

    @Override
    public User save(User user) {
        UserDO userDO = UserConverter.toDO(user);
        userMapper.insertOrUpdate(userDO);
        return UserConverter.toDomain(userDO);
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(userMapper.selectById(id))
                .map(UserConverter::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, username);
        return Optional.ofNullable(userMapper.selectOne(wrapper))
                .map(UserConverter::toDomain);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getPhone, phone);
        return Optional.ofNullable(userMapper.selectOne(wrapper))
                .map(UserConverter::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getEmail, email);
        return Optional.ofNullable(userMapper.selectOne(wrapper))
                .map(UserConverter::toDomain);
    }

    @Override
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getUsername, username);
        return userMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsByPhone(String phone) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getPhone, phone);
        return userMapper.selectCount(wrapper) > 0;
    }

    @Override
    public boolean existsByEmail(String email) {
        LambdaQueryWrapper<UserDO> wrapper = new LambdaQueryWrapper<UserDO>()
                .eq(UserDO::getEmail, email);
        return userMapper.selectCount(wrapper) > 0;
    }
}