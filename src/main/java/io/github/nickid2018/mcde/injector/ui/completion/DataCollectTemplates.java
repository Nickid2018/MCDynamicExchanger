package io.github.nickid2018.mcde.injector.ui.completion;

import org.fife.ui.autocomplete.CompletionProvider;
import org.fife.ui.autocomplete.DefaultCompletionProvider;
import org.fife.ui.autocomplete.TemplateCompletion;

public class DataCollectTemplates {

    public static void makeDataCollectTemplate(DefaultCompletionProvider provider) {
        provider.addCompletion(getCompletionTemplate(
                provider, "adat", "aload", "Ljava/lang/Object;"));
        provider.addCompletion(getCompletionTemplate(
                provider, "idat", "iload", "I"));
        provider.addCompletion(getCompletionTemplate(
                provider, "ldat", "lload", "J"));
        provider.addCompletion(getCompletionTemplate(
                provider, "ddat", "dload", "D"));
        provider.addCompletion(getCompletionTemplate(
                provider, "fdat", "fload", "F"));

        provider.addCompletion(getCompletionTimeoutTemplate(
                provider, "adatt", "aload", "Ljava/lang/Object;"));
        provider.addCompletion(getCompletionTimeoutTemplate(
                provider, "idatt", "iload", "I"));
        provider.addCompletion(getCompletionTimeoutTemplate(
                provider, "ldatt", "lload", "J"));
        provider.addCompletion(getCompletionTimeoutTemplate(
                provider, "ddatt", "dload", "D"));
        provider.addCompletion(getCompletionTimeoutTemplate(
                provider, "fdatt", "fload", "F"));
    }

    private static TemplateCompletion getCompletionTemplate(CompletionProvider provider,
                                                            String input, String loadType, String inputType) {
        TemplateCompletion completion = new TemplateCompletion(provider, input, "DataCollector.put", """
                ldc string ${data_name}
                %s ${data_load}
                invokestatic io/github/nickid2018/mcde/data/DataCollector.put(Ljava/lang/String;%s)V false
                """.formatted(loadType, inputType));
        completion.getParam(0).setDescription("Name of the data");
        completion.getParam(1).setDescription("Local Variable Slot of the data");
        return completion;
    }

    private static TemplateCompletion getCompletionTimeoutTemplate(CompletionProvider provider,
                                                                   String input, String loadType, String inputType) {
        TemplateCompletion completion = new TemplateCompletion(provider, input, "DataCollector.putWithTimeout", """
                ldc string ${data_name}
                %s ${data_load}
                ldc long ${timeout}
                invokestatic io/github/nickid2018/mcde/data/DataCollector.putWithTimeout(Ljava/lang/String;%sJ)V false
                """.formatted(loadType, inputType));
        completion.getParam(0).setDescription("Name of the data");
        completion.getParam(1).setDescription("Local Variable Slot of the data");
        completion.getParam(2).setDescription("Timeout of recording");
        return completion;
    }
}
