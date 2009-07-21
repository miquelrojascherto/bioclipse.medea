//gist: 151205
//Demonstrates basic manipulation to predict a EI-MS in Bioclipse using
//a sdf file containing a list of molecule. 
//Requires medea data installed in default location
var mols = cdk.loadMolecules("/Sample Data/SDFfiles/CAS.5.sdf");
for (var i = 0; i < mols.size(); i++) {
	var mol = mols.get(i);
	mol = cdk.generate2dCoordinates(mol);
	ui.open(mol);

	cmlSpectrum = medea.predictMassSpectrum(mol)
	spectrum.saveSpectrum(cmlSpectrum,"Virtual/predictedSpectrum_"+i+".cml","cml")
//	ui.open("/Virtual/predictedSpectrum_"++".cml");
}

