//Demonstrates basic manipulation to predict a EI-MS in Bioclipse
//Requires medea data installed in default location
mol = cdk.loadMolecule("/Sample Data/Molecules/30460-92-5-2d.mol");
mol = cdk.generate2dCoordinates(mol);
ui.open(mol);

cmlSpectrum = medea.predictMassSpectrum(mol)
spectrum.saveSpectrum(cmlSpectrum,"Virtual/predictedSpectrum.cml","cml")
