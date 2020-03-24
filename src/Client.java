import java.time.LocalTime;

public class Client {
	private String code;
	private String cpf;
	private int priority;
	private LocalTime estimatedTime;
	private LocalTime arrivalTime;
	
	public Client(String code, String cpf, int priority, LocalTime estimatedTime, LocalTime arrivalTime) {
		this.code = code;
		this.cpf = cpf;
		this.priority = priority;
		this.estimatedTime = estimatedTime;
		this.arrivalTime = arrivalTime;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getCpf() {
		return cpf;
	}
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public LocalTime getEstimatedTime() {
		return estimatedTime;
	}
	public void setEstimatedTime(LocalTime estimatedTime) {
		this.estimatedTime = estimatedTime;
	}
	public LocalTime getArrivalTime() {
		return arrivalTime;
	}
	public void setArrivalTime(LocalTime arrivalTime) {
		this.arrivalTime = arrivalTime;
	}
	
	@Override
	public String toString() {
		return "\nCode: " + this.getCode() + " CPF: " + this.getCpf() + " | " + this.getPriority() +
				" | \nArrive: " + this.getArrivalTime() + "\tEstimated: " + this.getEstimatedTime();
	}
	
	public String minimize() {
		return "\nCode: " + this.getCode() + "\tArrived: " + this.getArrivalTime();
	}
	
}
