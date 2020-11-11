package cn.allams.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService myUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailsService).passwordEncoder(password());
    }
    @Bean
    PasswordEncoder password() {
        return new BCryptPasswordEncoder();
    }
    // 其他配置
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() //自定义自己编写的登陆页面
            .loginPage("/login.html") //登录页面设置
            .loginProcessingUrl("/user/login") //登录界面的url
            .defaultSuccessUrl("/test/echo").permitAll() //登陆成功后默认跳转路径
            .and().authorizeRequests()
                .antMatchers("/", "/user/login", "/test/hello").permitAll() //设置不需要认证的路径
            .anyRequest().authenticated()
                .and().csrf().disable(); //关闭csrf
    }

    // 配置账号密码方式
    /*
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // 加密密码
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");
        // 配置用户名密码和角色
        auth.inMemoryAuthentication().withUser("allams").password(password).roles("admin");
    }
    */
}
