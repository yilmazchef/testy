package be.intecbrussel.student.views.component;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class RankingButton extends VerticalLayout {

    public enum Stars {
        ONE(1.00),
        ONE_AND_HALF(1.50),
        TWO(2.00),
        TWO_AND_HALF(2.50),
        THREE(3.00),
        THREE_AND_HALF(3.50),
        FOUR(4.00),
        FOUR_AND_HALF(4.50),
        FIVE(5.00);

        private final Double value;

        Stars(Double value) {
            this.value = value;
        }

        public Double getValue() {
            return value;
        }
    }

    private Stars stars;

    public RankingButton() {
        this.stars = Stars.FIVE;
    }

    public RankingButton(Stars stars) {

        final var starFull = VaadinIcon.STAR.create();
        final var starHalfLeft = VaadinIcon.STAR_HALF_LEFT_O.create();
        final var starHalfRight = VaadinIcon.STAR_HALF_RIGHT_O.create();
        final var starEmpty = VaadinIcon.STAR_O.create();

        final var buttons = new Button[]{
                new Button(starFull),
                new Button(starFull),
                new Button(starFull),
                new Button(starFull),
                new Button(starFull)
        };

        switch (stars) {
            case ONE:
                for (Button button : buttons) {
                    button.addClickListener(onClick -> {
                        if (button.getIcon().equals(starFull)) {
                            button.setIcon(starEmpty);
                        } else {
                            button.setIcon(starFull);
                        }
                    });
                }
                break;
            case ONE_AND_HALF:

                break;
            case TWO:
                break;
            case TWO_AND_HALF:

                break;
            case THREE:


                break;
            case THREE_AND_HALF:

                break;
            case FOUR:

                break;
            case FOUR_AND_HALF:

                break;
            case FIVE:


                break;
            default:

                break;
        }

        this.stars = stars;

        add(buttons);
    }

    public Stars getStars() {
        return stars;
    }

    public void setStars(Stars stars) {
        this.stars = stars;
    }

    public RankingButton withStars(Stars stars) {
        this.setStars(stars);
        return this;
    }
}
