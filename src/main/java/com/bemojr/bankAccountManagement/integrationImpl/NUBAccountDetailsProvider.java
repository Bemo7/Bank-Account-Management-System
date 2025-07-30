package com.bemojr.bankAccountManagement.integrationImpl;

import com.bemojr.bankAccountManagement.dto.AccountCodeAndNumberDto;
import com.bemojr.bankAccountManagement.dto.BankAccountDetailsDto;
import com.bemojr.bankAccountManagement.dto.CreateAccountRequestDto;
import com.bemojr.bankAccountManagement.exception.ResourceNotFoundException;
import com.bemojr.bankAccountManagement.integration.AccountDetailsProvider;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.QueryMap;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
@Slf4j
public class NUBAccountDetailsProvider implements AccountDetailsProvider {
    @Value("${api-base-url}")
    String apiBaseUrl;

    @Autowired
    public OkHttpClient okHttpClient;

    @Override
    public BankAccountDetailsDto call(AccountCodeAndNumberDto userQueryRequestDto) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)  // base URL
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()) // JSON converter
                .build();

        log.info("Base URL -> {}", apiBaseUrl);

        ApiService apiService = retrofit.create(ApiService.class);

        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("account_number", userQueryRequestDto.accountNumber());

        if (userQueryRequestDto.bankCode() != null) {
            queryParams.put("bank_code", userQueryRequestDto.bankCode());
        }

        Call<Map<String, Object>> call = apiService.getAccountDetails(headers, queryParams);

        Response<Map<String, Object>> response;

        try {
            response = call.execute();
        } catch (IOException e) {
            throw new ResourceNotFoundException("Unable to reach third party verification API");
        }

        if (!response.isSuccessful()) {
            try(
                    ResponseBody responseBody = response.errorBody()
            ) {
                String errorBodyString = responseBody != null ? responseBody.string() : "Unknown error";
                throw new ResourceNotFoundException(errorBodyString);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new ResourceNotFoundException("Failed to read error body");
            }
        } else {
            if (Objects.nonNull(response.body())) {
                Map<String, Object> responseMap = response.body();

                if (responseMap.containsKey("message")) {
                    throw new ResourceNotFoundException(responseMap.get("message").toString());
                }
                return this.transform(responseMap);
            } else {
                throw new ResourceNotFoundException("No response body found");
            }
        }
    }

    public BankAccountDetailsDto transform(Map<String, Object> map) {
        BankAccountDetailsDto bankAccountDetailsDto = new BankAccountDetailsDto();
        bankAccountDetailsDto.setAccountNumber((String) map.get("account_number"));
        bankAccountDetailsDto.setAccountName((String) map.get("account_name"));
        bankAccountDetailsDto.setFirstName((String) map.get("first_name"));
        bankAccountDetailsDto.setLastName((String) map.get("last_name"));
        bankAccountDetailsDto.setOtherName((String) map.get("other_name"));
        bankAccountDetailsDto.setBankName((String) map.get("Bank_name"));
        bankAccountDetailsDto.setBankCode((String) map.get("bank_code"));
        return bankAccountDetailsDto;
    }
}

interface ApiService {
    @GET("verify")
    Call<Map<String, Object>> getAccountDetails(@HeaderMap Map<String, String> headers, @QueryMap() Map<String, String> queryParams);
}