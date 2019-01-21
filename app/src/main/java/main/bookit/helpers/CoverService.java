package main.bookit.helpers;

import java.util.HashMap;

import main.bookit.R;

public class CoverService {
    private HashMap<String, Integer> covers;

    public CoverService(){
        covers = new HashMap<>();
        covers.put("yW5hQpeif0i9rhenE6dc69", R.drawable.cb_the_stand_stephen_king);
        covers.put("nRWys4YDUtju4pcr3qFLw2", R.drawable.cb_killing_commendatore_haruki_murakami);
        covers.put("beRo4KeK5zOOcn62imWtSo", R.drawable.cb_fire_and_blood_george_martin);
        covers.put("PIW4sP1sFJgLtE01opmy2H", R.drawable.cb_stan_lee_bob_batchelor);
        covers.put("M8S6iOBippsCr7jCCCd2NE", R.drawable.cb_the_choice_edith_eger);
        covers.put("I9Cb38uNWzzPVCarxzBe6F", R.drawable.cb_every_breath_nicholas_sparks);
        covers.put("CpHF70aaK6XeLjhs5RrCP0u", R.drawable.cb_cool_book_of_cool_people_some_cool_author);
        covers.put("85rrGvXHafx5pc49VL9dCea", R.drawable.cb_the_wind_up_bird_chronicle_haruki_murakami);
        covers.put("85rrGvXHafx5pc49VL9Fvca", R.drawable.cb_the_elephant_vanishes_haruki_murakami);
    }

    //temporary workaround of getting cover
    public int getCover(String bookId){
        return covers.get(bookId);
    }
}
