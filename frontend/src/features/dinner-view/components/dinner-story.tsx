import SectionHeader from "@/components/shared/section-header";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { GuestDinnerViewDto, HostDinnerViewDto } from "@/types";

type Props = {
  dinner: HostDinnerViewDto | GuestDinnerViewDto;
};

const DinnerStory = ({ dinner }: Props) => (
  <section>
    <SectionHeader title="The Story" className="mb-4" />
    <Card className="pt-0 overflow-hidden">
      <CardHeader className="px-0">
        <img src={`data:image/jpeg;base64,${dinner.storyBannerData}`} className="w-full h-64 object-cover" />
      </CardHeader>
      <CardContent>
        <h4 className="text-xl font-semibold mb-2">{dinner.storyTitle}</h4>
        <div className="space-y-4">
          {dinner.dinnerStoryBrief
            .split(/\r?\n/)
            .filter(Boolean)
            .map((line, idx) => (
              <p key={idx}>{line}</p>
            ))}
        </div>
      </CardContent>
    </Card>
  </section>
);

export default DinnerStory;
