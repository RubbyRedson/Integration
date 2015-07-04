package ru.riskgap.integration.models.tfs;

/**
 * Created by Nikita on 04.07.2015.
 */
public class FieldValuePair {
	private final String field;

	private final String value;

	private final boolean newField;

	public FieldValuePair(String field, String value, boolean newField) {
		this.field = field;
		this.value = value;
		this.newField = newField;
	}

	public String getField() {
		return field;
	}

	public String getValue() {
		return value;
	}

	public boolean isNewField() {
		return newField;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FieldValuePair that = (FieldValuePair) o;

		if (field != null ? !field.equals(that.field) : that.field != null) return false;
		if (value != null ? !value.equals(that.value) : that.value != null) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = field != null ? field.hashCode() : 0;
		result = 31 * result + (value != null ? value.hashCode() : 0);
		return result;
	}

	@Override
	public String toString() {
		return "FieldValuePair{" +
				"field='" + field + '\'' +
				", value='" + value + '\'' +
				'}';
	}
}
