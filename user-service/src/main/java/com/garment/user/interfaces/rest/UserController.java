package com.garment.user.interfaces.rest;

import com.garment.common.interfaces.R;
import com.garment.user.application.identity.UserAppService;
import com.garment.user.application.identity.dto.LoginCommand;
import com.garment.user.application.identity.dto.RegisterCommand;
import com.garment.user.domain.identity.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 用户身份 REST 控制器
 */
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserAppService userAppService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<Map<String, Object>> register(@Valid @RequestBody RegisterCommand cmd) {
        User user = userAppService.register(cmd);
        return R.ok(Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "nickname", user.getNickname()
        ));
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<Map<String, Object>> login(@Valid @RequestBody LoginCommand cmd,
                                        HttpServletRequest request) {
        String loginIp = getClientIp(request);
        Map<String, Object> result = userAppService.login(cmd, loginIp);
        return R.ok(result);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/{userId}")
    public R<Map<String, Object>> getUser(@PathVariable Long userId) {
        User user = userAppService.getUserById(userId);
        return R.ok(Map.of(
                "userId", user.getId(),
                "username", user.getUsername(),
                "nickname", user.getNickname() != null ? user.getNickname() : "",
                "avatar", user.getAvatar() != null ? user.getAvatar() : "",
                "phone", user.getPhone() != null ? user.getPhone().getValue() : "",
                "email", user.getEmail() != null ? user.getEmail().getValue() : "",
                "status", user.getStatus().name()
        ));
    }

    /**
     * 修改密码
     */
    @PutMapping("/{userId}/password")
    public R<Void> changePassword(@PathVariable Long userId,
                                  @RequestBody Map<String, String> body) {
        userAppService.changePassword(userId, body.get("oldPassword"), body.get("newPassword"));
        return R.ok();
    }

    /**
     * 更新用户资料
     */
    @PutMapping("/{userId}/profile")
    public R<Void> updateProfile(@PathVariable Long userId,
                                 @RequestBody Map<String, String> body) {
        userAppService.updateProfile(userId, body.get("nickname"), body.get("avatar"));
        return R.ok();
    }

    /**
     * 冻结用户
     */
    @PutMapping("/{userId}/freeze")
    public R<Void> freezeUser(@PathVariable Long userId) {
        userAppService.freezeUser(userId);
        return R.ok();
    }

    /**
     * 解冻用户
     */
    @PutMapping("/{userId}/unfreeze")
    public R<Void> unfreezeUser(@PathVariable Long userId) {
        userAppService.unfreezeUser(userId);
        return R.ok();
    }

    /**
     * 注销用户
     */
    @DeleteMapping("/{userId}")
    public R<Void> deactivateUser(@PathVariable Long userId) {
        userAppService.deactivateUser(userId);
        return R.ok();
    }

    /**
     * 获取客户端 IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}