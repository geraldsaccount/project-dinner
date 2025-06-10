import GridLayout from "@/components/layout/grid-layout";
import { DinnerParticipantDto } from "@/types";
import CharacterProfile from "./character-profile";

type Props = {
  participants: DinnerParticipantDto[];
  yourCharacterUuid: string;
};

const CastGrid = ({ participants, yourCharacterUuid }: Props) => {
  const others = participants.filter(
    (p) => p.character.uuid !== yourCharacterUuid
  );
  return (
    <GridLayout gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}>
      {others.map((p, i) => (
        <CharacterProfile key={i} user={p.user} character={p.character} />
      ))}
    </GridLayout>
  );
};

export default CastGrid;
