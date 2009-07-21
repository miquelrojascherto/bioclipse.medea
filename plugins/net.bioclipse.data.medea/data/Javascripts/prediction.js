//gist: 151207
//Demonstrates basic manipulation to predict a EI-MS in Bioclipse
//Requires medea data installed in default location
mol = cdk.loadMolecule("/Sample Data/Molecules/30460-92-5-2d.mol");
mol = cdk.generate2dCoordinates(mol);
ui.open(mol);

cmlReactionScheme = medea.predictMassSpectrum(mol)
reaction.saveReactionScheme(cmlReactionScheme,"Virtual/predictedFragmenation.cml")
ui.open("/Virtual/predictedFragmenation.cml");