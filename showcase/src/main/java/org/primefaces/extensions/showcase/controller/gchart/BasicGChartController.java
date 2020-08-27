package org.primefaces.extensions.showcase.controller.gchart;

import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.primefaces.event.SelectEvent;
import org.primefaces.extensions.component.gchart.model.DefaultGChartModelColumn;
import org.primefaces.extensions.component.gchart.model.GChartModel;
import org.primefaces.extensions.component.gchart.model.GChartModelBuilder;
import org.primefaces.extensions.component.gchart.model.GChartType;

@Named
@RequestScoped
public class BasicGChartController implements Serializable {

    private static final long serialVersionUID = 253762400419864192L;

    private final Random random = new Random();
    private int mushrooms = random.nextInt(10);
    private int onions = random.nextInt(10);
    private GChartType chartType = GChartType.PIE;
    private GChartModel chartModel = null;

    public GChartModel getChart() {
        return chartModel;
    }

    @PostConstruct
    public void generateModel() {
        // tooltip use values
        final HashMap<String, String> tooltip = new HashMap<>();
        tooltip.put("text", "value");

        // color each slice
        final List<String> colors = Arrays.asList("#F39C12", "#7D3C98");

        chartModel = new GChartModelBuilder().setChartType(getChartType())
                    .addOption("colors", colors)
                    .addOption("tooltip", tooltip)
                    .addOption("pieSliceText", "percentage")
                    .addColumns(new DefaultGChartModelColumn("Topping", "string"),
                                new DefaultGChartModelColumn("Slices", "number"))
                    .addRow("Mushrooms", mushrooms).addRow("Onions", onions).build();
    }

    public void onSelect(final SelectEvent event) {
        final JsonArray value = (JsonArray) event.getObject();
        if (value.size() > 0) {
            final JsonElement element = value.get(0);
            final String label = new ArrayList<>(getChart().getRows())
                        .get(element.getAsJsonObject().get("row").getAsInt()).getLabel();
            FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_INFO, "You have selected: " + label, null));
        }
    }

    public int getMushrooms() {
        return mushrooms;
    }

    public void setMushrooms(final int mushrooms) {
        this.mushrooms = mushrooms;
    }

    public int getOnions() {
        return onions;
    }

    public void setOnions(final int onions) {
        this.onions = onions;
    }

    public GChartType getChartType() {
        return chartType;
    }

    public void setChartType(final GChartType chartType) {
        this.chartType = chartType;
    }

    public List<GChartType> getTypes() {
        return Arrays.asList(new GChartType[] {GChartType.AREA, GChartType.BAR, GChartType.COLUMN, GChartType.PIE,
                    GChartType.LINE});
    }
}