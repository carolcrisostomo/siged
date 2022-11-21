package br.com.siged.util;

public class Anexo {
	
	private String filename;
	private byte[] file;
	private FileType fileType;
	
	public Anexo(String filename, byte[] file, FileType fileType) {
		this.filename = filename;
		this.file = file;
		this.fileType = fileType;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public byte[] getFile() {
		return file;
	}

	public FileType getFileType() {
		return fileType;
	}
	
	public String getCompleteFilename() {
		return this.filename + this.fileType.getExtension();
	}
	
	public String getContentType() {
		return this.fileType.getContentType();
	}

	/**
	 * 
	 * Enum com as principais extensões de arquivos usadas como anexos de email e pelos relatórios do sistema:
	 * <dl>
     * <dt>PDF		<dd>.pdf ; application/pdf
     * <dt>CSV		<dd>.csv ; text/csv
     * <dt>TXT		<dd>.txt ; text/plain
     * </dl>
	 *
	 */
	public enum FileType{
		PDF(".pdf", "application/pdf"),
		CSV(".csv", "text/csv"),
		TXT(".txt", "text/plain");
		
		private String extension;
		private String contentType;
		
		FileType(String extension, String contentType) {
			this.extension = extension;
			this.contentType = contentType;
		}
		
		public String getExtension() {
			return this.extension;
		}
		
		public String getContentType() {
			return this.contentType;
		}
	}
}
