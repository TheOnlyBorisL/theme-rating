package net.etfbl.themerating.model.entity;

import com.amazonaws.services.comprehend.model.DominantLanguage;
import com.amazonaws.services.comprehend.model.Entity;
import com.amazonaws.services.comprehend.model.KeyPhrase;
import com.amazonaws.services.comprehend.model.SentimentScore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ComprehendResult {
    private String RequestText;

    private List<Entity> NamedEntities;
    private List<KeyPhrase> KeyPhrases;
    private List<DominantLanguage> Language;
    private SentimentScore Sentiment;

}
