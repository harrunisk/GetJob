package com.example.harun.getjob.AddJobAdvert;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by mayne on 28.06.2018.
 */

public interface SuggestionsKeysInterface  {
    void getSuggestionsKeysCallback(HashMap<String, ArrayList<SuggestionsModel>> keyResultHash);

}
