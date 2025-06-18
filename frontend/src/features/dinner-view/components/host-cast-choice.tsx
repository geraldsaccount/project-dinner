import GridLayout from "@/components/layout/grid-layout";
import CharacterCard from "@/components/shared/character-card";
import SectionHeader from "@/components/shared/section-header";
import { HostDinnerViewDto } from "@/types";
import CharacterChoiceSection from "./character-choice-section";
import { useState } from "react";

type Props = {
  dinner: HostDinnerViewDto;
};

const HostCastChoice = ({ dinner }: Props) => {
  const [accepting, setAccepting] = useState(false);

  return (
    <section>
      <SectionHeader title="Guests & Characters" className="mb-4" />
      <p className="text-center text-sm text-destructive font-semibold mb-4">
        Please choose a Character before inviting your guests.
      </p>
      <GridLayout gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}>
        {dinner.participants.map((p) => (
          <CharacterCard key={p.character.uuid} character={p.character}>
            <CharacterChoiceSection
              inviteCode={
                dinner.assignments.find(
                  (a) => a.characterId === p.character.uuid
                )?.inviteCode || ""
              }
              accepting={accepting}
              setAccepting={setAccepting}
            />
          </CharacterCard>
        ))}
      </GridLayout>
    </section>
  );
};

export default HostCastChoice;
