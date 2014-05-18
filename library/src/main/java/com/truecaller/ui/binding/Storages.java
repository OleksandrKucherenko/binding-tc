package com.truecaller.ui.binding;

import java.lang.reflect.AccessibleObject;

/** Access to different types of storages. */
public final class Storages {
    /** hidden constructor */
    private Storages() {
        throw new AssertionError();
    }

    public static <T extends AccessibleObject> Matcher<T> property(final String name) {
        return null;
    }

}
