package com.github.org.projectnova.extrasforftb.common.utils;

/**
 * <ul>
 *     <li>Used for accessing new methods or fields in Mixin classes</li>
 * </ul>
 *
 * See <a href="https://mixin-wiki.readthedocs.io/tricks/">Mixin Wiki/Mixin Tricks</a>
 *
 * @author X_Niter
 */
public interface MixinAccessor {
    boolean vanished();

    void setVanished(boolean vanished);
}
