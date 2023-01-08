package dc.food_diary.ui.main_tabs.recipe;

public class Recipe {
    private String name;
    private String url;
    private String img;
    private String category;
    private String summary;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    @Override
    public String toString() {
        StringBuilder dataBuilder = new StringBuilder();
        appendFieldValue(dataBuilder, name);
        appendFieldValue(dataBuilder, url);
        appendFieldValue(dataBuilder, img);
        appendFieldValue(dataBuilder, category);
        appendFieldValue(dataBuilder, summary);

        return dataBuilder.toString();
    }

    private void appendFieldValue(StringBuilder dataBuilder, String fieldValue) {
        if (fieldValue != null) {
            dataBuilder.append(fieldValue).append(";");
        } else {
            dataBuilder.append("").append(";");
        }
    }
}
