package net.etfbl.themerating.service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.*;
import com.google.gson.Gson;
import net.etfbl.themerating.model.entity.ComprehendResult;
import net.etfbl.themerating.model.entity.Content;
import net.etfbl.themerating.model.request.ContentRequest;
import net.etfbl.themerating.model.response.ContentResponse;
import net.etfbl.themerating.repository.ContentRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContentService {

    private final ContentRepository _contentRepository;

    public ContentService(ContentRepository contentRepository){
        _contentRepository = contentRepository;
    }

    public void CreateContent(ContentRequest request){
        ComprehendResult comprehendResult = new ComprehendResult();
        Gson gson = new Gson();
        AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();
        AmazonComprehend comprehendClient =
                AmazonComprehendClientBuilder.standard()
                        .withCredentials(awsCreds)
                        .withRegion("eu-central-1")
                        .build();
        comprehendResult.setRequestText(request.getInformation());

        DetectDominantLanguageResult dominantLanguageResult =
                comprehendClient
                        .detectDominantLanguage(
                                new DetectDominantLanguageRequest()
                                        .withText(request.getInformation())
                        );

        if (dominantLanguageResult.getLanguages().stream()
                .anyMatch(dominantLanguage ->
                        dominantLanguage.getLanguageCode().equals("en") && dominantLanguage.getScore() > 0.9)) {

            comprehendResult.setSentiment(
                comprehendClient.detectSentiment(
                        new DetectSentimentRequest()
                                .withText(request.getInformation())
                                .withLanguageCode("en")
                ).getSentimentScore()
            );

            comprehendResult.setKeyPhrases(
                comprehendClient.detectKeyPhrases(
                        new DetectKeyPhrasesRequest()
                                .withText(request.getInformation())
                                .withLanguageCode("en")
                ).getKeyPhrases()
            );

            comprehendResult.setNamedEntities(
                comprehendClient.detectEntities(
                        new DetectEntitiesRequest()
                                .withLanguageCode("en")
                                .withText(request.getInformation())
                ).getEntities()
            );
        }

        comprehendResult.setLanguage(
            comprehendClient
                    .detectDominantLanguage(
                            new DetectDominantLanguageRequest()
                                    .withText(request.getInformation()))
                    .getLanguages());

        Content result = new Content();
        result.setInfo(gson.toJson(comprehendResult));
        result.setDateTime(Date.valueOf(LocalDate.now()));
        result.setClientID(request.getClientID());
        _contentRepository.saveAndFlush(result);
    }

    public List<ContentResponse> getClientContent(long ClientID){
        return _contentRepository
                .findAllByClientIDEquals(ClientID)
                .stream()
                .map(x -> {
                    ContentResponse res = new ContentResponse();
                    res.setInformation(x.getInfo());
                    return res;
                })
                .collect(Collectors.toList());
    }
}
