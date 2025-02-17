package com.DigitalShop.DigitalTreasure.Configs;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfLoggingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String csrfToken = request.getHeader("X-XSRF-TOKEN");
        System.out.println("Получен CSRF токен: " + csrfToken);



        chain.doFilter(request, response);
    }
}

