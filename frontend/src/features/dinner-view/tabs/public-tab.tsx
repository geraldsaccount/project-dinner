import SectionHeader from "@/components/shared/section-header";
import { PreDinnerInfoDto } from "@/types";

type Props = {
  preDinnerInfo: PreDinnerInfoDto;
};

const PublicTab = ({ preDinnerInfo }: Props) => {
  return (
    <div className="space-y-8">
      <section>
        <SectionHeader title="Background Story" />
        {preDinnerInfo.setting
          .split(/\r?\n\r?\n/)
          .filter((paragraph) => paragraph.trim() !== "")
          .map((paragraph, idx) => (
            <p key={idx}>{paragraph}</p>
          ))}
      </section>
      <section>
        <SectionHeader title="Rules" />
        {preDinnerInfo.rules
          .split(/\r?\n\r?\n/)
          .filter((paragraph) => paragraph.trim() !== "")
          .map((paragraph, idx) => (
            <p key={idx}>{paragraph}</p>
          ))}
      </section>
      <section>
        <SectionHeader title="Participants" />
        <ul className="list-disc list-inside">
          {preDinnerInfo.participants
            .filter((participant) => participant.user)
            .map((participant, index) => (
              <li key={index} className="mb-1">
                <span className="font-bold">{participant.user?.name}</span>
                {participant.user && ` (as ${participant.character.name})`}
              </li>
            ))}
        </ul>
      </section>
    </div>
  );
};

export default PublicTab;
