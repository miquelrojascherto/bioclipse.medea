mol = cdk.loadMolecule("/SampleDataMedea/Medea/molecule/30460-92-5-2d.mol");
mol = cdk.generate2dCoordinates(mol);
ui.open(mol);

cmlSpectrum = medea.predictMassSpectrum(mol)
spectrum.saveSpectrum(cmlSpectrum,"Virtual/predictedSpectrum.cml","cml")
