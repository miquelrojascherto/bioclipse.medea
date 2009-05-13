mol = cdk.fromSMILES("C=C1C(=O)CC2CC1C2(C)(C)");
mol = cdk.generate2dCoordinates(mol);
ui.open(mol);

cmlSpectrum = medea.predictMassSpectrum(mol)
spectrum.saveSpectrum(cmlSpectrum,"Virtual/predictedSpectrum.cml","cml")
