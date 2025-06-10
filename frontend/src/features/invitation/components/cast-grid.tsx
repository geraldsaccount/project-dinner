import GridLayout from "@/components/layout/grid-layout";
import { DinnerParticipantDto } from "@/types";
import CharacterCard from "@/components/shared/character-card";
import UserAvatar from "@/components/shared/user-avatar";

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
      {others.map((p) => (
        <CharacterCard key={p.character.uuid} character={p.character}>
          {p.user ? (
            <div className="flex items-center gap-3">
              <UserAvatar user={p.user} className="w-12 h-12" />
              <div>
                <p className="text-xs text-muted-foreground">Played by</p>
                <p className="font-semibold">{p.user.name}</p>
              </div>
            </div>
          ) : (
            <p className="text-center text-sm font-semibold text-muted-foreground">
              TBD
            </p>
          )}
        </CharacterCard>
      ))}
    </GridLayout>
  );
};

export default CastGrid;
