package servlets.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import java.io.IOException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import app.utils.JsonData;

import java.io.BufferedReader;

@WebFilter(urlPatterns = { "/api/*" }, filterName = "ApiFilter")
public class ApiFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        System.out.println("ApiFilter doFilter");
        StringBuilder jsonBuffer = new StringBuilder();
        String line;
        try (BufferedReader reader = request.getReader()) {
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        }

        // Convert JSON string to JsonObject
        String jsonString = jsonBuffer.toString();
        Gson gson = new Gson();
        try {
            JsonObject jsonObject = gson.fromJson(jsonString, JsonObject.class);
            request.setAttribute("data", new JsonData(jsonObject));
        } catch (Exception e) {
            response.getWriter().write("{\"message\": \"Invalid JSON\"}");
            // set status code to 400
            ((HttpServletResponse) response).setStatus(400);
            response.setContentType("application/json");
            return;
        }

        chain.doFilter(request, response);
    }
}
