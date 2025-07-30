package com.bemojr.bankAccountManagement.configuration;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class BaseConfiguration {
    @Value("${api-key}")
    String apiKey;

    @Bean
    public Interceptor interceptor() {
        return chain -> {
            Request originalRequest = chain.request();
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .build();
            return chain.proceed(newRequest);
        };
    }

    @Bean
    public OkHttpClient okHttpClient(
            Interceptor interceptor
    ) {
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
//                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();
    }
}
