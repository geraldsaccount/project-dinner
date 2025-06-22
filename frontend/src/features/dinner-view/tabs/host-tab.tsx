import GridLayout from "@/components/layout/grid-layout";
import CharacterCard from "@/components/shared/character-card";
import SectionHeader from "@/components/shared/section-header";
import UserAvatar from "@/components/shared/user-avatar";
import { HostDinnerViewDto } from "@/types";
import InviteLinkSection from "../components/invite-link-section";
import HostCastChoice from "../components/host-cast-choice";
import { Button } from "@/components/ui/button";
import { useAuthenticatedApi } from "@/hooks";

type Props = { dinner: HostDinnerViewDto };

const HostTab = ({ dinner }: Props) => {
  const { loading, error, callApi } = useAuthenticatedApi<HostDinnerViewDto>();

  const progressDinner = async () => {
    await callApi(`/api/dinners/${dinner.preDinnerInfo.uuid}/progress`, "PUT");
    if (!error) {
      window.location.reload();
    }
  };

  const renderControls = () => {
    if (dinner.conclusion?.motive) return null;

    const section = (label: string, btn: string) => (
      <section>
        <SectionHeader title={label} />
        <div className="w-full flex justify-center">
          <Button disabled={loading} onClick={progressDinner}>
            {btn}
          </Button>
        </div>
      </section>
    );

    if (dinner.hostInfo.allHaveVoted)
      return section("Dinner Controls", "Conclude Mystery");
    if (dinner.conclusion) return section("Dinner Controls", "Start Vote");
    if (dinner.hostInfo.stagePrompts && dinner.hostInfo.stagePrompts.length > 0)
      return section("Dinner Controls", "Next Stage");
    if (!dinner.hostInfo.assignments.some((a) => !a.userId))
      return section("Dinner Controls", "Start Dinner");
    return null;
  };
  console.log(dinner.hostInfo.stagePrompts)

  return (
    <div className="space-y-8">
      {renderControls()}
      {dinner.hostInfo.stagePrompts &&
        !dinner.conclusion?.motive && (
          <section>
            <SectionHeader title="Stage Prompt" />
            <p>Read the following prompt out loud to your guests.</p>
            {dinner.hostInfo.stagePrompts[
              dinner.hostInfo.stagePrompts.length - 1
            ]
              .split(/\r?\n\r?\n/)
              .filter((paragraph) => paragraph.trim() !== "")
              .map((paragraph, idx) => (
                <p key={idx} className="leading-relaxed mb-4">
                  {paragraph}
                </p>
              ))}
          </section>
        )}

      <section>
        <SectionHeader title="Guest Tracker" />
        {!dinner.privateInfo ? (
          <HostCastChoice dinner={dinner} />
        ) : (
          <GridLayout gridCols={{ base: 1, sm: 2, md: 2, xl: 3 }}>
            {dinner.preDinnerInfo.participants.map((p) => (
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
                  <InviteLinkSection
                    inviteCode={
                      dinner.hostInfo.assignments.find(
                        (a) => a.characterId === p.character.uuid
                      )?.inviteCode || ""
                    }
                  />
                )}
              </CharacterCard>
            ))}
          </GridLayout>
        )}
      </section>
      <section>
        <SectionHeader title="Host Briefing" />
        {dinner.hostInfo.briefing
          .split(/\r?\n\r?\n/)
          .filter((paragraph) => paragraph.trim() !== "")
          .map((paragraph, idx) => (
            <p key={idx} className="leading-relaxed mb-4">
              {paragraph}
            </p>
          ))}
      </section>
    </div>
  );
};

export default HostTab;
