package br.edu.ufcg.dsc.ia.coppelia.evolution.chromossome;

import java.util.Random;

public abstract class Action {
    private static final int TRANSLATION_LIMIT = 1000;
    private static final int ROTATION_LIMIT = 360;

    protected String name;
    protected String parameter;

    public Action(String name) {
        this.name = name;
    }

    public String name() {
        return this.name;
    }

    public String parameter() {
        return this.parameter;
    }

    public abstract Action opposite();

    @Override
    public String toString() {
        return this.name() + "(" + this.parameter() + ")";
    }

    /* ABSTRACT CHILDREN */

    private static abstract class RandomAction extends Action {
        protected Random rng;

        public RandomAction(String name, Random rng) {
            super(name);
            this.rng = rng;
        }
    }

    private static abstract class Translation extends RandomAction {
        public Translation(String name, Random rng) {
            super(name, rng);
            this.parameter = String.valueOf(rng.nextInt(TRANSLATION_LIMIT));
        }
    }

    private static abstract class Rotation<T extends Rotation<T>> extends RandomAction {
        protected boolean dependsOnOpponentBearing;

        public Rotation(String name, Random rng) {
            super(name, rng);
            this.parameter = String.valueOf(rng.nextInt(ROTATION_LIMIT));
        }

        @SuppressWarnings("unchecked")
        protected T dependingOnOpponentBearing() {
            this.dependsOnOpponentBearing = true;
            this.parameter = "event.getBearing() + " + this.parameter;
            return (T) this;
        }

        @Override
        public Action opposite() {
            return this.dependsOnOpponentBearing
                    ? this.oppositeDependingOnOpponentBearing()
                    : this.naturalOpposite();
        }

        protected abstract Action naturalOpposite();

        protected abstract Action oppositeDependingOnOpponentBearing();

    }

    private static abstract class GunRotation extends RandomAction {
        public GunRotation(String name, Random rng) {
            super(name, rng);
            this.parameter = this.buildParameterExpressionWith(
                    "getHeading()",
                    "getGunHeading()",
                    "event.getBearing()",
                    String.valueOf(rng.nextInt(ROTATION_LIMIT)));
        }

        private String buildParameterExpressionWith(String... strings) {
            StringBuilder parameterExpression = new StringBuilder();

            for (int i = 0; i < strings.length; i++) {
                parameterExpression
                        .append("(" + this.randomSignum() + ") * " + strings[i])
                        .append(i == strings.length - 1 ? "" : " + ");
            }

            return parameterExpression.toString();
        }

        private int randomSignum() {
            if (this.rng.nextBoolean()) {
                return 1;
            }

            return -1;
        }
    }

    /* CONCRETE CHILDREN */

    public static class SetAhead extends Translation {
        public SetAhead(Random rng) {
            super("setAhead", rng);
        }

        @Override
        public Action opposite() {
            return new SetBack(this.rng);
        }
    }

    public static class SetBack extends Translation {
        public SetBack(Random rng) {
            super("setBack", rng);
        }

        @Override
        public Action opposite() {
            return new SetAhead(this.rng);
        }
    }

    public static class SetTurnRight extends Rotation<SetTurnRight> {
        public SetTurnRight(Random rng) {
            super("setTurnRight", rng);
        }

        @Override
        protected Action naturalOpposite() {
            return new SetTurnLeft(this.rng);
        }

        @Override
        protected Action oppositeDependingOnOpponentBearing() {
            return new SetTurnLeft(this.rng).dependingOnOpponentBearing();
        }
    }

    public static class SetTurnLeft extends Rotation<SetTurnLeft> {
        public SetTurnLeft(Random rng) {
            super("setTurnLeft", rng);
        }

        @Override
        protected Action naturalOpposite() {
            return new SetTurnRight(this.rng);
        }

        @Override
        protected Action oppositeDependingOnOpponentBearing() {
            return new SetTurnRight(this.rng).dependingOnOpponentBearing();
        }
    }

    public static class SetTurnRadarRight extends Rotation<SetTurnRadarRight> {
        public SetTurnRadarRight(Random rng) {
            super("setTurnRadarRight", rng);
        }

        @Override
        protected Action naturalOpposite() {
            return new SetTurnRadarLeft(this.rng);
        }

        @Override
        protected Action oppositeDependingOnOpponentBearing() {
            return new SetTurnRadarLeft(this.rng).dependingOnOpponentBearing();
        }
    }

    public static class SetTurnRadarLeft extends Rotation<SetTurnRadarLeft> {
        public SetTurnRadarLeft(Random rng) {
            super("setTurnRadarLeft", rng);
        }

        @Override
        protected Action naturalOpposite() {
            return new SetTurnRadarRight(this.rng);
        }

        @Override
        protected Action oppositeDependingOnOpponentBearing() {
            return new SetTurnRadarRight(this.rng).dependingOnOpponentBearing();
        }
    }

    public static class SetTurnGunRight extends GunRotation {
        public SetTurnGunRight(Random rng) {
            super("setTurnGunRight", rng);
        }

        @Override
        public Action opposite() {
            return new SetTurnGunLeft(this.rng);
        }
    }

    public static class SetTurnGunLeft extends GunRotation {
        public SetTurnGunLeft(Random rng) {
            super("setTurnGunLeft", rng);
        }

        @Override
        public Action opposite() {
            return new SetTurnGunRight(this.rng);
        }
    }

    public static class SetFire extends RandomAction {
        public SetFire(Random rng) {
            super("setFire", rng);
            this.parameter = String.valueOf(this.randomBetween(0.001, 0.003) * this.randomBetween(100, 1000));
        }

        @Override
        public Action opposite() {
            return new DoNothing(this.rng);
        }

        private double randomBetween(double a, double b) {
            return a + (this.rng.nextDouble() * (b - a));
        }
    }

    public static class DoNothing extends RandomAction {
        public DoNothing(Random rng) {
            super("doNothing", rng);
            this.parameter = "";
        }

        @Override
        public Action opposite() {
            return new SetFire(this.rng);
        }
    }
}
