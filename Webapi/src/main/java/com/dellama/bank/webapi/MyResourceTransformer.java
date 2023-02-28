package com.dellama.bank.webapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.resource.ResourceTransformer;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class MyResourceTransformer implements ResourceTransformer {

    @Autowired
    private Environment env;

    @Override
    public Resource transform(HttpServletRequest request, Resource resource, ResourceTransformerChain transformerChain) throws IOException {
        if (request.getServletPath().equals("/propertiesLoader.js")) {
            byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
            String content = new String(bytes, StandardCharsets.UTF_8);
            String fullBankApiUrl = env.getProperty("bank.bankhostname") + ":" + env.getProperty("server.port");
            content = content.replace("var fullBankApiUrl = 'AUTO_FILLED'",
                    "var fullBankApiUrl = '" + fullBankApiUrl + "'");

            return new TransformedResource(resource, content.getBytes(StandardCharsets.UTF_8));
        } else {
            return resource;
        }
    }
}
