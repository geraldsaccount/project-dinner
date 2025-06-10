import { sampleGuestDinnerView, sampleHostDinnerView } from "@/data/sample-dinner-views";
import DinnerHeader from "./components/dinner-header";
import DinnerStory from "./components/dinner-story";
import DinnerCast from "./components/dinner-cast";
import DinnerSecret from "./components/dinner-secret";

const DinnerPage = () => {
  const dinner = sampleGuestDinnerView;

  return (
    <div className="flex flex-col gap-8">
      <DinnerHeader dinner={dinner} />
      <DinnerStory dinner={dinner} />
      <DinnerCast dinner={dinner} />
      <DinnerSecret
        character={
          dinner.participants.find(
            (p) => p.character.uuid === dinner.yourPrivateInfo.characterId
          )!.character
        }
        secret={dinner.yourPrivateInfo}
      />
    </div>
  );
};

export default DinnerPage;
