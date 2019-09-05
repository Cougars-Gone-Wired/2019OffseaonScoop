package frc.robot;

public class SwitchSides implements System {

    public SwitchSides() {
        initialize();
    }

    @Override
    public void initialize() {
        side = Sides.HATCH_SIDE;
    }

    @Override
    public void control(Controllers controllers) {
        switchSides(controllers.isSwitchSide());
    }

    public enum Sides {
        HATCH_SIDE, CARGO_SIDE
    }

    public static Sides side;

    public void switchSides(boolean sideToggle) {
        switch(side) {
            case HATCH_SIDE:
                if(sideToggle) {
                    side = Sides.CARGO_SIDE;
                }
                break;
            case CARGO_SIDE:
                if(!sideToggle) {
                    side = Sides.HATCH_SIDE;
                }
                break;
        }
    }
}