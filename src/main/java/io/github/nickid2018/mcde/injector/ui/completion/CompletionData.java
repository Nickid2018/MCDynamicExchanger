package io.github.nickid2018.mcde.injector.ui.completion;

import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;

public class CompletionData {

    public static CompletionProvider getProvider() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        BasicKeywordCompletion.keywords(provider);
        DataCollectTemplates.makeDataCollectTemplate(provider);
        return provider;
    }
}
