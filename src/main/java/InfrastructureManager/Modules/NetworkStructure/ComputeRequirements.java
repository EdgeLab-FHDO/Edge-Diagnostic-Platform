package InfrastructureManager.Modules.NetworkStructure;


public class ComputeRequirements {
	private float requiredCpu;
	private float requiredGpu;
	private float requiredMemory;
	
	public ComputeRequirements(float requiredCpu,float requiredGpu, float requiredMemory) {
        this.requiredCpu = requiredCpu;
        this.requiredGpu = requiredGpu;
        this.requiredMemory = requiredMemory;
    }
	
	public float getRequiredCpu() {
		return requiredCpu;
	}
	
	public float getRequiredGpu() {
		return requiredGpu;
	}
	
	public float getRequiredMemory() {
		return requiredMemory;
	}
		
}