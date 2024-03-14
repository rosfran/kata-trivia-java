package trivia;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Player {

    public static final int MAXIMUM_PLACE_INDEX = 12;

    private int id;
    private String name;
    private int place;
    private int purse;
    private boolean inPenaltyBox;
    private boolean isGettingOutOfPenaltyBox;

    public void move(int roll) {
        place += roll;
        if (place >=MAXIMUM_PLACE_INDEX) place -= MAXIMUM_PLACE_INDEX;
    }

    public void incrementPurse() {
        purse++;
    }


}
