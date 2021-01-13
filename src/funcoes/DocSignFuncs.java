package funcoes;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Scanner;
import util.Base64;

public class DocSignFuncs {
	private String docName = "";
	private String fileName = "";
	private String CN = "";
	private byte[] docBytes;
	
	public DocSignFuncs(String doc, String file) {
		this.setDocName(doc);
		this.setFileName(file);
		
		this.readInfoFromCert();
		this.readInfoFromDoc();
	}
	
	public void createTxtFile() {
		File txtFile = new File("DocSigned.txt");
		try {
			if (txtFile.createNewFile()) {
				System.out.println("File created: " + txtFile.getName());
				
				FileWriter writer = new FileWriter("DocSigned.txt");
				
				writer.write("-----BEGIN DOCSIGNED-----");
				writer.write("\ndoc:" + this.getDocName());
				writer.write("\nalg:RSA");
				writer.write("\nhash:SHA1");
				writer.write("\nassinante:" + this.getCN());
				writer.write("\n\n-----BEGIN DOC-----\n" + this.getDocBytes());
				writer.write("\n-----END DOC-----");
				writer.write("\n-----BEGIN SIGNATURE-----");
				writer.write("\n-----END SIGNATURE-----");
				writer.write("\n-----END DOCSIGNED-----");
				
				writer.close();
				
			    System.out.println("Successfully wrote to the file.");
			}
			else {
				System.out.println("File already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void readInfoFromCert() {
		try {
			KeyStore p12 = KeyStore.getInstance("pkcs12");
			p12.load(new FileInputStream("SAS_Yeda.p12"), "Seguranca".toCharArray());
			Enumeration<String> e;
			e = p12.aliases();
			while (e.hasMoreElements()) {
	            String alias = e.nextElement();
	            X509Certificate c;
	            c = (X509Certificate) p12.getCertificate(alias);
	            Principal subject = c.getSubjectDN();
	            String subjectArray[] = subject.toString().split(",");
	            for (String s : subjectArray) {
	                String[] str = s.trim().split("=");
	                String key = str[0];
	                String value = str[1];
	                
	                if (key.equals("CN")) {
	                	this.setCN(value);
	                	System.out.println(key + " - " + value);
	                }
	                
	            }
	        }
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
	}
	
	private void readInfoFromDoc() {
		BufferedReader myBuffer;
		try {
			myBuffer = new BufferedReader(new InputStreamReader(new FileInputStream("DocTeste.txt"), "UTF-8"));
			
			String linha = myBuffer.readLine();
			String docMsg = linha;
			
			while (linha != null) {
				//System.out.println(linha);
				linha = myBuffer.readLine();
				docMsg += linha;
			}
			
			myBuffer.close();
			
			System.out.println(docMsg);
			
			this.setDocBytes(Base64.encode(docMsg.getBytes()));
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setDocName(String doc){
    	this.docName = doc;
    }
	
	public String getDocName(){
        return this.docName;
    }
	
	public void setFileName(String file){
    	this.fileName = file;
    }
	
	public String getFileName(){
        return this.fileName;
    }
	
	public void setCN(String cn){
    	this.CN = cn;
    }
	
	public String getCN(){
        return this.CN;
    }
	
	public void setDocBytes(byte[] db){
    	this.docBytes = db;
    }
	
	public byte[] getDocBytes(){
        return this.docBytes;
    }
}
