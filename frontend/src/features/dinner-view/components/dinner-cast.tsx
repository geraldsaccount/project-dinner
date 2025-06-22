import GridLayout from "@/components/layout/grid-layout";
import SectionHeader from "@/components/shared/section-header";
import { GuestDinnerViewDto, HostDinnerViewDto } from "@/types";
import CharacterCard from "../../../components/shared/character-card";
import UserAvatar from "@/components/shared/user-avatar";
import InviteLinkSection from "./invite-link-section";

type Props = {
  dinner: HostDinnerViewDto | GuestDinnerViewDto;
};

const DinnerCast = ({ dinner }: Props) => {
  const isHostDinnerView = (
    dinner: HostDinnerViewDto | GuestDinnerViewDto
  ): dinner is HostDinnerViewDto => {
    return "assignments" in dinner;
  };

  return (
    <section>
      {/* <SectionHeader title="Guests & Characters" className="mb-4" />
      <GridLayout gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}>
        {dinner.participants.map((p) =>
          p.character.uuid === dinner.yourPrivateInfo.characterId ? null : (
            <CharacterCard key={p.character.uuid} character={p.character}>
              {p.user ? (
                <div className="flex items-center gap-3">
                  <UserAvatar user={p.user} className="w-12 h-12" />
                  <div>
                    <p className="text-xs text-muted-foreground">Played by</p>
                    <p className="font-semibold">{p.user.name}</p>
                  </div>
                </div>
              ) : isHostDinnerView(dinner) ? (
                <InviteLinkSection
                  inviteCode={
                    dinner.assignments.find(
                      (a) => a.characterId === p.character.uuid
                    )?.inviteCode || ""
                  }
                />
              ) : (
                <p className="text-center text-sm font-semibold text-muted-foreground">
                  Player not yet assigned
                </p>
              )}
            </CharacterCard>
          )
        )}
      </GridLayout> */}
    </section>
  );
};

export default DinnerCast;
