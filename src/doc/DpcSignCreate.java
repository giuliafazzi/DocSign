package doc;

import java.util.Scanner;
import funcoes.DocSignFuncs;

public class DpcSignCreate {
	public static void main(String[] args){
		Scanner scan = new Scanner(System.in);
		
	    System.out.println("Documento a ser assinado");
	    String docName = scan.nextLine(); 
	    

	    System.out.println("Nome do arquivo");
	    String fileName = scan.nextLine(); 
	    
	    DocSignFuncs doc = new DocSignFuncs(docName, fileName);
	    
	    doc.createTxtFile();
	}
}
