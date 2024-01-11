@SuppressWarnings("module")
module ch15.sec06 {
    requires jakarta.json.bind;
	opens com.horstmann.places to org.eclipse.yasson;
}
