package com.cartisan.management.modules.sys.shiro;

import com.cartisan.management.modules.sys.model.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;


/**
 * <p>Title: ShiroUtils</p>
 * <p>Description: </p>
 *
 * @author colin
 */
public class ShiroUtils {
    /**
     * 加密算法
     */
    public final static String hashAlgorithmName = "SHA-256";

    /**
     * 循环次数
     */
    public final static int hashIterations = 16;

    public static String sha256(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject(){
        return SecurityUtils.getSubject();
    }

    public static User getUser() {
        return (User) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUser().getUserId();
    }

    public static void setSessionattribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal()!=null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    public static String getKaptcha(String key) {
        Object kaptcha = getSessionAttribute(key);
        if (kaptcha == null) {
            throw new RuntimeException("验证码已失效。");
        }
        getSession().removeAttribute(key);

        return kaptcha.toString();
    }
}
