import SectionHeader from "@/components/shared/section-header";
import { CharacterStageDto } from "@/types";

type Props = { stages: CharacterStageDto[] };

const StagesTab = ({ stages }: Props) => {
    console.log(stages)
  return (
    <div>
      {stages.map((s, i) => (
        <div className="pb-4">
          <SectionHeader title={`Stage ${i + 1}: ${s.stageTitle}`} />

          {s.events.map((e) => (
            <div className="grid md:grid-cols-[180px_1fr]">
              <div className="md:border-r-2 border-primary pr-4 pb-4">
                <p className="hidden md:block font-bold text-base md:text-lg uppercase text-right leading-tight">
                  {e.time}
                </p>
                <p className="hidden md:block font-bold text-sm md:text-base text-right leading-tight">
                  {e.title}
                </p>
              </div>
              <div className="leading-relaxed block md:hidden font-bold text-sm space-x-2">
                <span>{e.time}</span>
                <span>{e.title}</span>
              </div>
              <p className="md:pl-4 pb-4 text-sm md:text-base leading-relaxed">
                {e.description}
              </p>
            </div>
          ))}
          <div className="pt-4 font-bold">{s.objectiveDesc}</div>
        </div>
      ))}
    </div>
  );
};

export default StagesTab;
