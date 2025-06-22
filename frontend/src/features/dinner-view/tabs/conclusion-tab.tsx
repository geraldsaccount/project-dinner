import CharacterAvatar from "@/components/shared/character-avatar";
import SectionHeader from "@/components/shared/section-header";
import { Separator } from "@/components/ui/separator";
import { ConclusionDto, PreDinnerInfoDto } from "@/types";

type Props = {
  preDinnerInfo: PreDinnerInfoDto;
  conclusion: ConclusionDto;
};

const ConclusionTab = ({ preDinnerInfo, conclusion }: Props) => {
  const criminals = preDinnerInfo.participants
    .filter((p) => conclusion.criminalIds.includes(p.character.uuid))
    .map((p) => p.character);
  return (
    <div className="space-y-8">
      <section>
        <SectionHeader title="Mystery Resolved!" />
        <h4 className="text-3xl md:text-4xl font-extrabold uppercase text-center mb-4">
          The criminals are:
        </h4>
        <div className="flex flex-wrap justify-center gap-4 mb-6">
          {criminals.map((c) => (
            <div
              key={c.uuid}
              className="flex flex-col items-center border-2 bg-destructive border-foreground p-4"
            >
              <CharacterAvatar
                character={c}
                className="w-24 h-24 border-2 border-primary object-cover mb-2"
              />

              <p className="font-bold text-lg uppercase text-destructive-foreground text-center">
                {c.name}
              </p>
              <p className="text-sm italic text-destructive-foreground text-center">
                {c.role}
              </p>
            </div>
          ))}
        </div>
        <h4 className="text-2xl font-bold uppercase text-center mb-2 pb-2">
          The Motive & Method:
        </h4>
        <Separator className="bg-foreground" />
        <p className="leading-relaxed text-center">{conclusion.motive}</p>
      </section>
      <section>
        <SectionHeader title="Vote Results" />
        {conclusion.votes.map((vote, index) => {
          const guestUser = preDinnerInfo.participants.find(
            (p) => p.user?.uuid === vote.guestId
          )?.user;
          const suspectCharacters = preDinnerInfo.participants
            .filter((p) => vote.suspectIds.includes(p.character.uuid))
            .map((p) => p.character);
          return (
            <div key={index} className="p-4 bg-neutral-100">
              <p className="font-bold uppercase mb-1">
                <span className="text-neutral-900">
                  {guestUser?.name || "Unknown Guest"}
                </span>{" "}
                Accused{" "}
                <span className="text-neutral-900">
                  {suspectCharacters
                    .map((c) => c.name)
                    .reduce((acc, name, idx, arr) => {
                      if (arr.length === 1) return name;
                      if (idx === 0) return name;
                      if (idx === arr.length - 1) return acc + " and " + name;
                      return acc + ", " + name;
                    }, "")}
                </span>
              </p>
              <p className="text-sm leading-relaxed border-t border-gray-400 pt-1">
                <span className="font-semibold">Motive:</span> {vote.motive}
              </p>
            </div>
          );
        })}
      </section>
    </div>
  );
};

export default ConclusionTab;
