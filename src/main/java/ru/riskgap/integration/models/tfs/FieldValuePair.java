package ru.riskgap.integration.models.tfs;

/**
 * A simple wrapper class for a field of a TFS task that should be created or updated
 * Created by Nikita on 04.07.2015.
 */
public class FieldValuePair {
	/**
	 * A value in TFS with the correct name of a Task field
	 */
	private final String field;

	/**
	 * A String value that represents the intended value of the field in TFS
	 */
	private final String value;

	/**
	 * A variable that indicates if the task already has this field in TFS
	 */
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
