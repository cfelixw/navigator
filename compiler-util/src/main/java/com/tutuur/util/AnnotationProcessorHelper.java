package com.tutuur.util;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * {@code AnnotationProcessor}帮助类
 *
 * @author yujie
 */
public class AnnotationProcessorHelper {

    private static final String MESSAGE_FORMAT = "[%s] %s";

    private final ProcessingEnvironment env;

    private final boolean debug;

    /**
     * 从ProcessingEnvironment获取相关参数，构造实例
     *
     * @param env   AnnotationProcessor对应的环境
     * @param debug 是否开启Debug模式.
     */
    public AnnotationProcessorHelper(ProcessingEnvironment env, boolean debug) {
        this.env = env;
        this.debug = debug;
    }

    public ProcessingEnvironment getEnv() {
        return env;
    }

    public String getPackageName(TypeElement element) {
        return env.getElementUtils()
                .getPackageOf(element)
                .getQualifiedName()
                .toString();
    }

    public boolean isString(TypeMirror type) {
        return isSameType(type, TypeConstants.FQDN_STRING);
    }

    /**
     * 判断是否是Activity
     *
     * @param type 待判断的类型
     * @return 返回{@code} 如果{@code type}继承自{@code Activity}
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isActivity(TypeMirror type) {
        return isAssignable(type, TypeConstants.FQDN_ACTIVITY);
    }

    /**
     * 判断是否是Fragment
     *
     * @param type 待判断的类型
     * @return 返回{@code} 如果{@code type}继承自{@code Fragment}
     */
    public boolean isFragment(TypeMirror type) {
        return isAssignable(type, TypeConstants.FQDN_FRAGMENT)
                || isAssignable(type, TypeConstants.FQDN_SUPPORT_FRAGMENT);
    }

    public boolean isSerializable(TypeMirror type) {
        return isAssignable(type, TypeConstants.FQDN_SERIALIZABLE);
    }

    public boolean isParcelable(TypeMirror type) {
        return isAssignable(type, TypeConstants.FQDN_PARCELABLE);
    }

    /**
     * 判断{@code type}是否可赋值到{@code targetFqdn}类型
     *
     * @param type       待判断的类型
     * @param targetFqdn 目标类型FQDN字符串
     * @return 返回{@code true}如果可以赋值
     */
    private boolean isAssignable(TypeMirror type, String targetFqdn) {
        Types types = env.getTypeUtils();
        Elements elements = env.getElementUtils();
        return types.isAssignable(type, elements.getTypeElement(targetFqdn) == null ?
                null : elements.getTypeElement(targetFqdn).asType());
    }


    /**
     * 判断{@code type}是否完全匹配{@code targetFqdn}类型
     *
     * @param type       待判断的类型
     * @param targetFqdn 目标类型FQDN字符串
     * @return 返回{@code true}如果完全匹配
     */
    private boolean isSameType(TypeMirror type, String targetFqdn) {
        Types types = env.getTypeUtils();
        Elements elements = env.getElementUtils();
        return types.isSameType(type, elements.getTypeElement(targetFqdn) == null ?
                null : elements.getTypeElement(targetFqdn).asType());
    }

    public void i(String tag, String message) {
        if (debug) {
            env.getMessager()
                    .printMessage(Diagnostic.Kind.NOTE, String.format(MESSAGE_FORMAT, tag, message));
        }
    }

    public void e(String tag, String message) {
        env.getMessager()
                .printMessage(Diagnostic.Kind.ERROR, String.format(MESSAGE_FORMAT, tag, message));
    }
}