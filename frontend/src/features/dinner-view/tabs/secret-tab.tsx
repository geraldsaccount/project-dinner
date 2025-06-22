import GridLayout from "@/components/layout/grid-layout";
import CharacterAvatar from "@/components/shared/character-avatar";
import CharacterCard from "@/components/shared/character-card";
import SectionHeader from "@/components/shared/section-header";
import UserAvatar from "@/components/shared/user-avatar";
import { DinnerParticipantDto, PrivateInfoDto } from "@/types";

type Props = {
  participants: DinnerParticipantDto[];
  privateInfo: PrivateInfoDto;
};

const SecretTab = ({ participants, privateInfo }: Props) => {
  const currentPlayerCharacter = participants.find(
    (p) => p.character.uuid === privateInfo.characterId
  )?.character;

  if (!currentPlayerCharacter) {
    return <></>;
  }

  return (
    <div className="space-y-8">
      <section>
        {currentPlayerCharacter ? (
          <>
            <div className="flex flex-col sm:flex-row items-center sm:items-start space-y-4 sm:space-y-0 sm:space-x-4 mb-4 border-b border-black pb-4">
              <CharacterAvatar
                character={currentPlayerCharacter}
                className="w-24 h-24 border-2 border-black object-cover flex-shrink-0"
              />

              <div className="text-center sm:text-left">
                <h3 className="text-2xl font-bold uppercase">
                  {currentPlayerCharacter.name} ({currentPlayerCharacter.age})
                </h3>
                <p className="text-lg italic">{currentPlayerCharacter.role}</p>
              </div>
            </div>
            {privateInfo.characterDescription
              .split(/\r?\n\r?\n/)
              .filter((paragraph) => paragraph.trim() !== "")
              .map((paragraph, idx) => (
                <p key={idx} className="leading-relaxed mb-4">
                  {paragraph}
                </p>
              ))}
          </>
        ) : (
          <p>Your character information is not available.</p>
        )}
      </section>

      <section>
        <SectionHeader title="Suspects" />
        <GridLayout gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}>
          {participants.map((p) =>
            p.character.uuid === privateInfo.characterId ? null : (
              <CharacterCard
                key={p.character.uuid}
                character={p.character}
                description={privateInfo.relationships[p.character.uuid]}
              >
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
                    Player not yet assigned
                  </p>
                )}
              </CharacterCard>
            )
          )}
        </GridLayout>
      </section>
    </div>
  );
};

export default SecretTab;
