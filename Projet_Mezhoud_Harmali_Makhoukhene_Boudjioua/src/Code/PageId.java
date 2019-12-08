package Code;

public class PageId {
	private int FileIdx;
	private int PageIdx;
	public PageId(int FileIdx,int PageIdx) {
		this.FileIdx=FileIdx;
		this.PageIdx=PageIdx;
	}
	public PageId(int FileIdx) {
		this.FileIdx=FileIdx;
	}
	public int getPageIdx() {
		return PageIdx;
		
	}
	public int getFileIdx() {
		return  FileIdx;
		
	}
	public void setPageIdx(int PageId){
		this.PageIdx=PageId;
		
	}
	public void setFileIdx(int FileId){
		 
		 this.FileIdx=FileId;
	}
	
}
