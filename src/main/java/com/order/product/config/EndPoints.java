package com.order.product.config;

public class EndPoints {
    public static final String[] STAFF_GET = {
            "/staff-api/**"
    };
    public static final String[] STAFF_POST = {
            "/staff-api/**"
    };
    public static final String[] PUBLIC_GET = {
            "/image-api/**",
            "/cart-api/**",
            "/ws/**",
    };
    public static final String[] PUBLIC_POST = {
            "/image-api/**",
            "/cart-api/**",
            "/ws/**",

    };
    public static final String[] USER_GET = {
            "/brand-api/**",
            "/product-unit-api/**",
            "/user-api/**",
            "/cart-api/**",

    };
    public static final String[] USER_POST = {
            "/user-api/**",
            "/cart-api/**"
    };
    public static final String FRONT_END_HOST = "http://localhost:3000";

}
