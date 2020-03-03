
package ysa3;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.neuroph.core.Connection;
import org.neuroph.core.Layer;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.Neuron;
import org.neuroph.core.Weight;
import org.neuroph.nnet.learning.BackPropagation;


public class MomentumBackpropagation extends BackPropagation {


    private static final long serialVersionUID = 1L;
    protected double momentum = 0.25d;
    private boolean durum = false;

    public MomentumBackpropagation() {
        super();
    }

    protected void calculateErrorAndUpdateHiddenNeurons() {
        List<Layer> layers = neuralNetwork.getLayers();
        for (int layerIdx = layers.size() - 2; layerIdx > 0; layerIdx--) {
            List<Neuron> layerNeurons = layers.get(layerIdx).getNeurons();
            if(layerNeurons.size() >= 100) {
				layerNeurons.parallelStream().forEach(neuron -> {
	                // calculate the neuron's error (delta)
	                double delta = calculateHiddenNeuronError(neuron);
	                neuron.setDelta(delta);
	                calculateWeightChanges(neuron);
	            });
            } else {
            	for(Neuron neuron : layerNeurons) {
            		// calculate the neuron's error (delta)
            		double delta = calculateHiddenNeuronError(neuron);
            		neuron.setDelta(delta);
            		calculateWeightChanges(neuron);
            	}
            }
        } // for
    }

    @Override
    public void calculateWeightChanges(Neuron neuron) {
        for (Connection connection : neuron.getInputConnections()) {
            double input = connection.getInput();
            if (input == 0) {
                continue;
            }
            
            double neuronDelta = neuron.getDelta();

            Weight<MomentumTrainingData> weight = connection.getWeight();
            
            MomentumTrainingData weightTrainingData = weight.getTrainingData();
            
            double weightChange = -learningRate * neuronDelta * input + momentum * weightTrainingData.previousWeightChange;
            weightTrainingData.previousWeightChange = weight.weightChange;
            
            if (isBatchMode() == false) {
                weight.weightChange = weightChange;
            } else { 
                weight.weightChange += weightChange;
            }
        }
    }

    public void birKereCagir()
    {
        if(durum == true)
           return;
        durum = true;
        onStart();
    }

    public double getMomentum() {
        return momentum;
    }


    public void setMomentum(double momentum) {
        this.momentum = momentum;
    }

    public static class MomentumTrainingData {
        public double previousWeightChange;
    }

    
    @Override
    protected void onStart() {
        super.onStart();
                 
        for (Layer layer : neuralNetwork.getLayers()) {
            for (Neuron neuron : layer.getNeurons()) {
                for (Connection connection : neuron.getInputConnections()) {
                    connection.getWeight().setTrainingData(new MomentumTrainingData());
                }
            } 
        } 
    }
}
