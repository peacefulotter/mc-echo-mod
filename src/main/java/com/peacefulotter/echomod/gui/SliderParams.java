package com.peacefulotter.echomod.gui;

import java.util.function.Consumer;

public record SliderParams(String key, double from, double to, double cur, Consumer<Double> onChange)
{
}
