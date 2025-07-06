package com.order.product.config;

import com.order.product.model.entity.Brand;
import com.order.product.model.entity.ProductUnit;
import com.order.product.service.brand.IBrandService;
import com.order.product.service.jwt.JwtFilter;
import com.order.product.service.productUnit.IProductUnitService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig {
    @Autowired
    private JwtFilter jwtFilter;

    @Autowired
    private IBrandService  brandService;

    @Autowired
    private IProductUnitService productUnitService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                config -> config
                        .requestMatchers(HttpMethod.GET, EndPoints.PUBLIC_GET).permitAll()
                        .requestMatchers(HttpMethod.POST, EndPoints.STAFF_POST).hasAnyRole("STAFF","ADMIN")
                        .requestMatchers(HttpMethod.GET, EndPoints.STAFF_GET).hasAnyRole("STAFF","ADMIN")
        );
        http.cors(cors -> {
            cors.configurationSource(request -> {
                CorsConfiguration corsConfig = new CorsConfiguration();
                corsConfig.addAllowedOrigin(EndPoints.FRONT_END_HOST);
                corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
                corsConfig.addAllowedHeader("*");
                return corsConfig;
            });
        });
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        return http.build();
    }

    @PostConstruct
    public void init() throws IOException {
        List<Brand> brandList = this.brandService.findAll();
        if (brandList.isEmpty()) {
            this.brandService.save(new Brand("Thực phẩm tươi sống"));
            this.brandService.save(new Brand("Đồ ăn chế biến"));
            this.brandService.save(new Brand("Bơ-Trứng-Sữa"));
            this.brandService.save(new Brand("Thực phẩm đông lạnh"));
            this.brandService.save(new Brand("Bánh kẹo các loại"));
            this.brandService.save(new Brand("Đồ hộp-Đồ khô"));
            this.brandService.save(new Brand("Dầu ăn-Gia vị-Nước chấm"));
            this.brandService.save(new Brand("Đồ uống đóng hộp"));
            this.brandService.save(new Brand("Nước giải khát"));
            this.brandService.save(new Brand("Đồ uống có cồn"));
            this.brandService.save(new Brand("Chăm sóc cá nhân"));
            this.brandService.save(new Brand("Vệ sinh nhà cửa"));
            this.brandService.save(new Brand("Chăm sóc thú cưng"));
            this.brandService.save(new Brand("Thực phẩm chức năng"));

        }
        List<ProductUnit> productUnits = this.productUnitService.getAllProductUnits();
        if (productUnits.isEmpty()) {
            this.productUnitService.save(new ProductUnit("Ký"));
            this.productUnitService.save(new ProductUnit("Cái"));
            this.productUnitService.save(new ProductUnit("Cuộn"));
            this.productUnitService.save(new ProductUnit("Chai"));
            this.productUnitService.save(new ProductUnit("Bình"));
            this.productUnitService.save(new ProductUnit("Lốc"));
            this.productUnitService.save(new ProductUnit("Gói"));
            this.productUnitService.save(new ProductUnit("Túi"));
            this.productUnitService.save(new ProductUnit("Vỉ"));
            this.productUnitService.save(new ProductUnit("Hộp"));
            this.productUnitService.save(new ProductUnit("Thùng"));
            this.productUnitService.save(new ProductUnit("Hũ"));
            this.productUnitService.save(new ProductUnit("Bịch"));
            this.productUnitService.save(new ProductUnit("Can"));
            this.productUnitService.save(new ProductUnit("Bao"));
        }
    }
}
