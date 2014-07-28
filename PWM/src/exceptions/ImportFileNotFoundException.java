package exceptions;

public class ImportFileNotFoundException extends Exception {
	 public ImportFileNotFoundException(){
		 super();
	 }
	 public ImportFileNotFoundException(String mes){
		 super(mes);
	 }
	 public ImportFileNotFoundException(String mes, Throwable cause){
		 super(mes, cause);
	 }
	 public ImportFileNotFoundException(Throwable cause){
		 super(cause);
	 }

}
