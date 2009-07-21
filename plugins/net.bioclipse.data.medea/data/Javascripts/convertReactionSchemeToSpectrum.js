//gist: 151372
//Demonstrates how to convert a CDKReactionScheme to ISpectrum
reactionScheme = reaction.loadReactionScheme("Sample Data/Reactions/reactionList.1.cml");
cmlSpectrum = medea.convertToSpectrum(reactionScheme)
spectrum.saveSpectrum(cmlSpectrum,"Virtual/spectrum.cml","cml")
ui.open("/Virtual/spectrum.cml");
